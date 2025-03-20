package com.innovation.xmlfilestorage.service;

import com.innovation.xmlfilestorage.exception.FileAlreadyExistsException;
import com.innovation.xmlfilestorage.exception.FileNotFoundException;
import com.innovation.xmlfilestorage.exception.XmlParsingException;
import com.innovation.xmlfilestorage.utils.FileUploadDirectoryInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.javapoet.FieldSpec;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class XmlFileProcessorService {

    private final FileUploadDirectoryInitializer directory;

    @Value("${json.indent-factor}")
    private int jsonIndentFactor;

    public void save(String fileName, MultipartFile multipartFile) {
        Path filePath = directory.getUploadDirectory().resolve(convertXmlToJsonFileName(fileName));

        if (Files.exists(filePath)) {
            throw new FileAlreadyExistsException(fileName);
        }

        toJson(filePath, fileName, multipartFile);
    }

    public String getFileByName(String fileName) {
        Path resolve = directory.getUploadDirectory().resolve(convertXmlToJsonFileName(fileName));
        try {
            return new String(Files.readAllBytes(resolve));
        } catch (IOException e) {
            throw new FileNotFoundException(fileName);
        }
    }

    public boolean update(String fileName, MultipartFile multipartFile) {
        Path filePath = directory.getUploadDirectory().resolve(convertXmlToJsonFileName(fileName));
        if (Files.exists(filePath)) {
            toJson(filePath, fileName, multipartFile);
            return true;
        }
        return false;
    }

    public boolean delete(String fileName) {
        return directory.getUploadDirectory()
                .resolve(fileName.concat(".json"))
                .toFile()
                .delete();
    }

    private void toJson(Path filePath, String fileName, MultipartFile multipartFile) {
        try {
            JSONObject json = XML.toJSONObject(new String(multipartFile.getBytes()));
            Files.writeString(filePath, json.toString(jsonIndentFactor));
        } catch (IOException | JSONException e) {
            log.error("Failed to parse XML file: {}", fileName, e);
            throw new XmlParsingException(fileName);
        }
    }

    private String convertXmlToJsonFileName(String fileName) {
        return fileName.replace(".xml", ".json");
    }
}

