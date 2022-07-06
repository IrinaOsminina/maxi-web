package ru.maxi.retail.osminina.scheduler;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.maxi.retail.osminina.entity.Sale;
import ru.maxi.retail.osminina.file.FileReader;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import ru.maxi.retail.osminina.entity.SaleRepository;

@Component
public class CheckLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckLoader.class);

    @Value("${file.directory}")
    private String filesDirectory;
    @Value("${file.directory.loaded}")
    private String loadedFilesDirectory;

    private FileReader fileReader;

    private SaleRepository saleRepository;

    public CheckLoader(SaleRepository saleRepository, FileReader fileReader) {
        this.saleRepository = saleRepository;
        this.fileReader = fileReader;
    }

    @Scheduled(fixedRate = 600000)
    public void load() {
        LOGGER.debug("Начало загрузки файлов из директориии {}", filesDirectory);
        Path pathWithCheck = Paths.get(filesDirectory);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(pathWithCheck)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    continue;
                }
                LOGGER.debug("Найден файл {}", path.getFileName().toString());

                List<Sale> sales = fileReader.parseXml(path.toFile());
                if (!sales.isEmpty()) {
                    LOGGER.debug("Из файла {} загрузили {} продаж", path.getFileName().toString(), sales.size());
                    LOGGER.debug("Сохраняем данные в БД");
                    saleRepository.saveAll(sales);
                    LOGGER.debug("Перемещаем обработанный файл в директорию {}", loadedFilesDirectory);
                    moveFileToLoadedPath(path);
                } else {
                    LOGGER.warn("В файле {} не найдено ни одной продажи");
                }
            }
        } catch (IOException e) {
            LOGGER.error("Во время загрузки чеков возникла ошибка: ", e);
        }
    }

    private void moveFileToLoadedPath(Path path) throws IOException {
        createDirectoryForLoadedFiles();
        try {
            Files.move(path, Paths.get(loadedFilesDirectory + "\\" + path.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("Ошибка при перемещении файла: ", e);
            throw e;
        }
    }

    private void createDirectoryForLoadedFiles() {
        Path pathForLoadedFile = Paths.get(loadedFilesDirectory);
        if (!Files.exists(pathForLoadedFile)) {
            try {
                Files.createDirectory(pathForLoadedFile);
            } catch (IOException e) {
                LOGGER.error("Не удалось создать директорию", e);
            }
        }
    }
}
