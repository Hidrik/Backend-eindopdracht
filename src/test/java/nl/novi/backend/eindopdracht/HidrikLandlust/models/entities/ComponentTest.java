package nl.novi.backend.eindopdracht.HidrikLandlust.models.entities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ComponentTest {
    @Test
    void componentAllGettersAndSettersOk() {
        Component comp = new Component();

        Long id = 1L;
        String description = "description";
        String manufacturer = "manufacturer";
        Integer price = 1000;
        Integer stock = 10;
        String articleNumber = "12345";
        String orderLink = "http://test.test/";
        String fileName = "file";
        String fileType = ".test";
        File fileData = new File("file.test");

        comp.setId(id);
        comp.setDescription(description);
        comp.setManufacturer(manufacturer);
        comp.setPrice(price);
        comp.setStock(stock);
        comp.setArticleNumber(articleNumber);
        comp.setOrderLink(orderLink);
        comp.setFileName(fileName);
        comp.setFileType(fileType);
        comp.setFileData(fileData);

        assertEquals(comp.getId(), id);
        assertEquals(comp.getDescription(), description);
        assertEquals(comp.getManufacturer(), manufacturer);
        assertEquals(comp.getPrice(), price);
        assertEquals(comp.getStock(), stock);
        assertEquals(comp.getArticleNumber(), articleNumber);
        assertEquals(comp.getOrderLink(), orderLink);
        assertEquals(comp.getFileName(), fileName);
        assertEquals(comp.getFileType(), fileType);
        assertEquals(comp.getFileData(), fileData);
    }
}
