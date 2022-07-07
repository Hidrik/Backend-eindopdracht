package nl.novi.backend.eindopdracht.hidriklandlust.models.entities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ComponentTest {
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
        String fileUrl = ".test";
        //File fileData = new File("file.test");

        comp.setId(id);
        comp.setDescription(description);
        comp.setManufacturer(manufacturer);
        comp.setPrice(price);
        comp.setStock(stock);
        comp.setArticleNumber(articleNumber);
        comp.setOrderLink(orderLink);
        comp.setFileName(fileName);
        comp.setFileUrl(fileUrl);
        //comp.setFileData(fileData);

        assertEquals(comp.getId(), id);
        assertEquals(comp.getDescription(), description);
        assertEquals(comp.getManufacturer(), manufacturer);
        assertEquals(comp.getPrice(), price);
        assertEquals(comp.getStock(), stock);
        assertEquals(comp.getArticleNumber(), articleNumber);
        assertEquals(comp.getOrderLink(), orderLink);
        assertEquals(comp.getFileName(), fileName);
        assertEquals(comp.getFileUrl(), fileUrl);
        //assertEquals(comp.getFileData(), fileData);
    }
}
