package test.vn.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import test.vn.utils.Constants;

@WebServlet("/image")
public class DownloadImageController
        extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        String fileName =
                req.getParameter("fname");

        if (fileName == null
                || fileName.isBlank()) {

            return;
        }

        File file =
                new File(
                    Constants.DIR,
                    fileName
                );

        if (!file.exists()) {

            resp.sendError(404);
            return;
        }

        String type =
                Files.probeContentType(
                    file.toPath()
                );

        if (type != null) {

            resp.setContentType(type);
        }

        Files.copy(
                file.toPath(),
                resp.getOutputStream()
        );
    }
}