package nl.novi.backend.eindopdracht.HidrikLandlust.controllers;

import nl.novi.backend.eindopdracht.HidrikLandlust.TestUtils;
import nl.novi.backend.eindopdracht.HidrikLandlust.dto.ComponentDto;
import nl.novi.backend.eindopdracht.HidrikLandlust.exceptions.RecordNotFoundException;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.ComponentService;
import nl.novi.backend.eindopdracht.HidrikLandlust.services.CustomUserDetailsService;
import nl.novi.backend.eindopdracht.HidrikLandlust.utils.FileStorage;
import nl.novi.backend.eindopdracht.HidrikLandlust.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static nl.novi.backend.eindopdracht.HidrikLandlust.TestUtils.generateComponentDto;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ComponentController.class)
public class ComponentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ComponentService componentService;

    @MockBean
    FileStorage fileStorage;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    DataSource dataSource;

    @MockBean
    JwtUtil jwtUtil;

    private final String admin = "ADMIN";
    private final String user = "USER";

    //TODO ADD ASSIGNMENT TO TESTS

    @Test
    @WithMockUser(roles = admin)
    void getAllComponentsAsAdminSucceeds() throws Exception {
        List<ComponentDto> dtos  = new ArrayList<>();
        ComponentDto dto = generateComponentDto();
        dtos.add(dto);

        when(componentService.getComponentsDto()).thenReturn(dtos);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/components"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", is(dto.getId().intValue())))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.[0].assignments", is(dto.getAssignments())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].price", is(dto.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].stock", is(dto.getStock())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fileName", is(dto.getFileName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].articleNumber", is(dto.getArticleNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].description", is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fileUrl", is(dto.getFileUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].manufacturer", is(dto.getManufacturer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].orderLink", is(dto.getOrderLink())));
    }

    @Test
    @WithMockUser(roles = user)
    void getAllComponentsAsUserSucceeds() throws Exception {
        List<ComponentDto> dtos  = new ArrayList<>();
        ComponentDto dto = generateComponentDto();
        dtos.add(dto);

        when(componentService.getComponentsDto()).thenReturn(dtos);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/components"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", is(dto.getId().intValue())))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.[0].assignments", is(dto.getAssignments())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].price", is(dto.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].stock", is(dto.getStock())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fileName", is(dto.getFileName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].articleNumber", is(dto.getArticleNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].description", is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fileUrl", is(dto.getFileUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].manufacturer", is(dto.getManufacturer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].orderLink", is(dto.getOrderLink())));
    }

    @Test
    void getAllComponentsUnauthorizedFails() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/components"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void getOneComponentAsAdminSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();

        when(componentService.getComponentDto(dto.getId())).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/components/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(dto.getId().intValue())))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.assignments", is(dto.getAssignments())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(dto.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", is(dto.getStock())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName", is(dto.getFileName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.articleNumber", is(dto.getArticleNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileUrl", is(dto.getFileUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manufacturer", is(dto.getManufacturer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLink", is(dto.getOrderLink())));
    }

    @Test
    @WithMockUser(roles = user)
    void getOneComponentAsUserSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();

        when(componentService.getComponentDto(dto.getId())).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/components/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(dto.getId().intValue())))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.assignments", is(dto.getAssignments())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(dto.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", is(dto.getStock())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName", is(dto.getFileName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.articleNumber", is(dto.getArticleNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileUrl", is(dto.getFileUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manufacturer", is(dto.getManufacturer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLink", is(dto.getOrderLink())));
    }

    @Test
    void getOneComponentUnauthenticatedFails() throws Exception {
        ComponentDto dto = generateComponentDto();

        when(componentService.getComponentDto(dto.getId())).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/components/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void createComponentAsAdminSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();

        when(componentService.createComponent(any(ComponentDto.class))).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/components")
                        .content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(dto.getId().intValue())))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.assignments", is(dto.getAssignments())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(dto.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", is(dto.getStock())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName", is(dto.getFileName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.articleNumber", is(dto.getArticleNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileUrl", is(dto.getFileUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manufacturer", is(dto.getManufacturer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLink", is(dto.getOrderLink())));
    }

    @Test
    @WithMockUser(roles = user)
    void createComponentAsUserSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();

        when(componentService.createComponent(any(ComponentDto.class))).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/components")
                        .content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(dto.getId().intValue())))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.assignments", is(dto.getAssignments())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(dto.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", is(dto.getStock())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName", is(dto.getFileName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.articleNumber", is(dto.getArticleNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileUrl", is(dto.getFileUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manufacturer", is(dto.getManufacturer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLink", is(dto.getOrderLink())));
    }

    @Test
    void createComponentUnauthorizedFails() throws Exception {
        ComponentDto dto = generateComponentDto();

        when(componentService.createComponent(any(ComponentDto.class))).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/components")
                        .content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void updateComponentAsAdminSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();

        when(componentService.updateComponent(any(Long.class), any(ComponentDto.class))).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .put(String.format("/components/%s", dto.getId()))
                        .content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(dto.getId().intValue())))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.assignments", is(dto.getAssignments())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(dto.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", is(dto.getStock())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName", is(dto.getFileName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.articleNumber", is(dto.getArticleNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileUrl", is(dto.getFileUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manufacturer", is(dto.getManufacturer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLink", is(dto.getOrderLink())));
    }

    @Test
    @WithMockUser(roles = user)
    void updateComponentAsUserSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();

        when(componentService.updateComponent(any(Long.class), any(ComponentDto.class))).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .put(String.format("/components/%s", dto.getId()))
                        .content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(dto.getId().intValue())))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.assignments", is(dto.getAssignments())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(dto.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", is(dto.getStock())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName", is(dto.getFileName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.articleNumber", is(dto.getArticleNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileUrl", is(dto.getFileUrl())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manufacturer", is(dto.getManufacturer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderLink", is(dto.getOrderLink())));
    }

    @Test
    void updateComponentUnauthorizedFails() throws Exception {
        ComponentDto dto = generateComponentDto();

        when(componentService.updateComponent(any(Long.class), any(ComponentDto.class))).thenReturn(dto);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .put(String.format("/components/%s", dto.getId()))
                        .content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void updateComponentAsAdminFailsNotFound() throws Exception {
        ComponentDto dto = generateComponentDto();
        String exceptionMessage = "exception";

        when(componentService.updateComponent(any(Long.class), any(ComponentDto.class))).thenThrow(new RecordNotFoundException(exceptionMessage));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .put(String.format("/components/%s", dto.getId()))
                        .content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }





    @Test
    @WithMockUser(roles = admin)
    void deleteComponentAsAdminSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();

        doNothing().when(componentService).deleteComponent(any(Long.class));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(String.format("/components/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @WithMockUser(roles = user)
    void deleteComponentAsUserSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();

        doNothing().when(componentService).deleteComponent(any(Long.class));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(String.format("/components/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    void deleteComponentUnauthorizedFails() throws Exception {
        ComponentDto dto = generateComponentDto();

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(String.format("/components/%s", dto.getId()))
                        .content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void deleteComponentAsAdminFailsNotFound() throws Exception {
        ComponentDto dto = generateComponentDto();
        String exceptionMessage = "exception";

        doThrow(new RecordNotFoundException(exceptionMessage)).when(componentService).deleteComponent(any(Long.class));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete(String.format("/components/%s", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }

    @Test
    @WithMockUser(roles = admin)
    void getComponentFileAsAdminSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();
        File file = new File("/");
        Resource resource = new UrlResource(file.toPath().toUri());

        when(componentService.loadFile(dto.getId())).thenReturn(resource);

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/components/%s/file", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes(resource.getInputStream().readAllBytes()));
    }

    @Test
    @WithMockUser(roles = user)
    void getComponentFileAsUserSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();
        File file = new File("/");
        Resource resource = new UrlResource(file.toPath().toUri());

        when(componentService.loadFile(dto.getId())).thenReturn(resource);

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/components/%s/file", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes(resource.getInputStream().readAllBytes()));
    }

    @Test
    void getComponentFileUnauthorizedFails() throws Exception {
        ComponentDto dto = generateComponentDto();
        File file = new File("/");
        Resource resource = new UrlResource(file.toPath().toUri());

        when(componentService.loadFile(dto.getId())).thenReturn(resource);

        mockMvc
                .perform(MockMvcRequestBuilders.get(String.format("/components/%s/file", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void saveComponentFileAsAdminSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();
        MockMultipartFile file =
                new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        doNothing().when(componentService).saveFile(any(Long.class), any(MultipartFile.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(String.format("/components/%s/file", dto.getId()))
                        .file("file", file.getBytes())
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())

                //Can't get file name returning to work
                .andExpect(content().string("Uploaded file successfully: "));
    }

    @Test
    @WithMockUser(roles = user)
    void saveComponentFileAsUserSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();
        MockMultipartFile file =
                new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        doNothing().when(componentService).saveFile(any(Long.class), any(MultipartFile.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(String.format("/components/%s/file", dto.getId()))
                        .file("file", file.getBytes())
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())

                //Can't get file name returning to work
                .andExpect(content().string("Uploaded file successfully: "));
    }

    @Test
    void saveComponentFileUnauthorizedFails() throws Exception {
        ComponentDto dto = generateComponentDto();
        MockMultipartFile file =
                new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());


        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(String.format("/components/%s/file", dto.getId()))
                        .file("file", file.getBytes())
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void saveComponentFileAsAdminFailsNoFileAttached() throws Exception {
        ComponentDto dto = generateComponentDto();

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(String.format("/components/%s/file", dto.getId())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason("Required request part 'file' is not present"));
    }

    @Test
    @WithMockUser(roles = admin)
    void saveComponentFileAsAdminFailsAlreadyFileAttached() throws Exception {
        ComponentDto dto = generateComponentDto();
        MockMultipartFile file =
                new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        when(componentService.hasFile(any(Long.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart(String.format("/components/%s/file", dto.getId()))
                        .file("file", file.getBytes())
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                //.andExpect(MockMvcResultMatchers.status().reason("Required request part 'file' is not present"));
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$", is("Component with id " + dto.getId() + " already has a file.")));
    }

    // I have NO IDEA how to test the put-request for updating component files.
    // This is because is use MockMvcRequestBuilders.multipart. I don't know how to set this request as post or put.


    @Test
    @WithMockUser(roles = admin)
    void deleteComponentFileAsAdminSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();


        mockMvc
                .perform(MockMvcRequestBuilders.delete(String.format("/components/%s/file", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$", is("Removed file from component with id " + dto.getId())));
    }

    @Test
    @WithMockUser(roles = user)
    void deleteComponentFileAsUserSucceeds() throws Exception {
        ComponentDto dto = generateComponentDto();


        mockMvc
                .perform(MockMvcRequestBuilders.delete(String.format("/components/%s/file", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$", is("Removed file from component with id " + dto.getId())));
    }

    @Test
    void deleteComponentFileUnauthorizedFails() throws Exception {
        ComponentDto dto = generateComponentDto();

        mockMvc
                .perform(MockMvcRequestBuilders.delete(String.format("/components/%s/file", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = admin)
    void deleteComponentFileAsAdminFailsComponentHasNoFile() throws Exception {
        ComponentDto dto = generateComponentDto();
        String exceptionMessage = "test";


        doThrow(new RecordNotFoundException(exceptionMessage)).when(componentService).deleteFile(dto.getId());

        mockMvc
                .perform(MockMvcRequestBuilders.delete(String.format("/components/%s/file", dto.getId())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(exceptionMessage)));
    }

}

