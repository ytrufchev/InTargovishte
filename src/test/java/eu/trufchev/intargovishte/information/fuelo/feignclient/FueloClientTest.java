package eu.trufchev.intargovishte.information.fuelo.feignclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FueloClientTest {

    private FueloClient fueloClient;

    @BeforeEach
    void setUp() {
        // Verify FeignClient annotation is present
        FeignClient feignClientAnnotation = FueloClient.class.getAnnotation(FeignClient.class);
        assertNotNull(feignClientAnnotation, "FeignClient annotation should be present");
        assertEquals("fueloClient", feignClientAnnotation.name(), "FeignClient name should match");
        assertEquals("https://fuelo.net/ajax/get_infowindow_content", feignClientAnnotation.url(), "FeignClient URL should match");
    }

    @Test
    void testGetGasstationDetailsMethodAnnotations() throws NoSuchMethodException {
        // Verify method annotations
        var method = FueloClient.class.getMethod("getGasstationDetails", String.class, String.class);

        // Check GetMapping annotation
        GetMapping getMappingAnnotation = method.getAnnotation(GetMapping.class);
        assertNotNull(getMappingAnnotation, "GetMapping annotation should be present");
        assertEquals("/{id}", getMappingAnnotation.value()[0], "GetMapping value should match");
        assertEquals("application/x-www-form-urlencoded", getMappingAnnotation.consumes()[0], "Consumes should be form-urlencoded");

        // Check PathVariable annotation on first parameter
        var idParam = method.getParameters()[0];
        var pathVariableAnnotation = idParam.getAnnotation(PathVariable.class);
        assertNotNull(pathVariableAnnotation, "PathVariable annotation should be present");
        assertEquals("id", pathVariableAnnotation.value(), "PathVariable should be 'id'");

        // Check RequestParam annotation on second parameter
        var langParam = method.getParameters()[1];
        var requestParamAnnotation = langParam.getAnnotation(RequestParam.class);
        assertNotNull(requestParamAnnotation, "RequestParam annotation should be present");
        assertEquals("lang", requestParamAnnotation.value(), "RequestParam should be 'lang'");
    }

    @Test
    void testGetGasstationDetailsMethodSignature() {
        // Verify method signature
        Class<?>[] parameterTypes = FueloClient.class.getMethods()[0].getParameterTypes();
        assertEquals(2, parameterTypes.length, "Method should have two parameters");
        assertEquals(String.class, parameterTypes[0], "First parameter should be String");
        assertEquals(String.class, parameterTypes[1], "Second parameter should be String");
    }

    @Test
    void testMethodReturnType() {
        // Verify return type
        Class<?> returnType = FueloClient.class.getMethods()[0].getReturnType();
        assertEquals(String.class, returnType, "Method should return String");
    }

    @Test
    void testInterfaceDesign() {
        // Verify interface is an interface
        assertTrue(FueloClient.class.isInterface(), "Should be an interface");
    }
}