package ru.maxi.retail.osminina.file;

import ru.maxi.retail.osminina.entity.Sale;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileReader {

    List<Sale> parseXml(File file) throws IOException;

}
