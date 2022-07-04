package nl.novi.backend.eindopdracht.HidrikLandlust.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.novi.backend.eindopdracht.HidrikLandlust.models.entities.Assignment;

import javax.persistence.*;
import java.io.File;
import java.util.Set;


public class ComponentDto {

    private Long id;
    private String description;
    private String manufacturer;
    private Integer price;
    private Integer stock;
    private String articleNumber;
    private String orderLink;

    private String fileName;
    private String fileUrl;

    @JsonSerialize
    Set<AssignmentSummaryDto> assignments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getOrderLink() {
        return orderLink;
    }

    public void setOrderLink(String orderLink) {
        this.orderLink = orderLink;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Set<AssignmentSummaryDto> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<AssignmentSummaryDto> assignments) {
        this.assignments = assignments;
    }
}
