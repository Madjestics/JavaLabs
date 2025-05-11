package org.example.Lab4;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.lab3.WordFrequencyProcessor;
import org.example.lab3.model.WordStatisticInfo;

import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private WordFrequencyProcessor processor;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() {
        processor = WordFrequencyProcessor.defaultProcessor();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String filename = req.getParameter("downloadFile");
        if (filename == null || filename.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан необходимый параметр");
            return;
        }

        File file;
        try {
            file = processor.openFile(filename);
        } catch (IOException e) {
            throw new ServletException("Ошибка при открытии файла", e);
        }
        if (file == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Файл не найден");
            return;
        }

        resp.setContentType(getServletContext().getMimeType(file.getName()));
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), resp.getOutputStream());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");               // ← важно!
        resp.setContentType("application/json; charset=UTF-8");
        var node = mapper.readTree(req.getReader());
        String word = node.path("word").asText(null);
        int limit = node.has("limit") ? node.path("limit").asInt(10) : 10;

        if (word == null || word.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Обязательно указание слова");
            return;
        }

        List<WordStatisticInfo> results;
        try {
            results = processor.searchByWord(word, limit);
        } catch (SQLException e) {
            throw new ServletException("Ошибка при поиске в БД", e);
        }

        resp.setContentType("application/json; charset=UTF-8");
        mapper.writeValue(resp.getWriter(), results);
    }
}

