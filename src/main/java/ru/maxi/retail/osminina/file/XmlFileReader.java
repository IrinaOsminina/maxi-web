package ru.maxi.retail.osminina.file;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;
import ru.maxi.retail.osminina.entity.Sale;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class XmlFileReader implements FileReader {

    public List<Sale> parseXml(File file) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(file, new TypeReference<List<Sale>>() {
        });
    }


}
