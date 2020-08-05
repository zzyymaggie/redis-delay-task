package clientSide;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.zzyymaggie.entity.MessageDTO;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * DelayTaskController Tester.
 *
 * @author zhangyu
 * @version 1.0
 * @since <pre>07/24/2020</pre>
 */
public class DelayTaskControllerTest {
    private RestTemplate restTemplate = new RestTemplate();
    private String baseUrl = "http://localhost:8093";

    @Test
    public void testProduceMessage() throws Exception {
        String requestBody = "[\n" +
                "  {\n" +
                "    \"callbackType\": 0,\n" +
                "    \"callbackUrl\": \"http://localhost:8093/delay/test\",\n" +
                "    \"taskInfo\": {\n" +
                "      \"expiredTime\": 0,\n" +
                "      \"interval\": 1000,\n" +
                "      \"msgBody\": \"string\",\n" +
                "      \"topic\": \"string\"\n" +
                "    }\n" +
                "  }\n" +
                "]";
        List<MessageDTO> messageDTOS = JSON.parseObject(requestBody,new TypeReference<List<MessageDTO>>(){});
        String result = restTemplate.postForObject(baseUrl + "/delay/produce", messageDTOS, String.class);
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testProduceMessageWith404Callback() throws Exception {
        String requestBody = "[\n" +
                "  {\n" +
                "    \"callbackType\": 0,\n" +
                "    \"callbackUrl\": \"http://localhost:8093/notfound\",\n" +
                "    \"taskInfo\": {\n" +
                "      \"expiredTime\": 0,\n" +
                "      \"interval\": 1000,\n" +
                "      \"msgBody\": \"string\",\n" +
                "      \"topic\": \"string\"\n" +
                "    }\n" +
                "  }\n" +
                "]";
        List<MessageDTO> messageDTOS = JSON.parseObject(requestBody,new TypeReference<List<MessageDTO>>(){});
        String result = restTemplate.postForObject(baseUrl + "/delay/produce", messageDTOS, String.class);
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testProduceMessageWithoutExpiredTime() throws Exception {
        String requestBody = "[\n" +
                "  {\n" +
                "    \"callbackType\": 0,\n" +
                "    \"callbackUrl\": \"http://localhost:8093/notfound\",\n" +
                "    \"taskInfo\": {\n" +
                "      \"expiredTime\": 0,\n" +
                "      \"interval\": 0,\n" +
                "      \"msgBody\": \"string\",\n" +
                "      \"topic\": \"string\"\n" +
                "    }\n" +
                "  }\n" +
                "]";
        List<MessageDTO> messageDTOS = JSON.parseObject(requestBody,new TypeReference<List<MessageDTO>>(){});
        String result = restTemplate.postForObject(baseUrl + "/delay/produce", messageDTOS, String.class);
        assertNotNull(result);
        System.out.println(result);
    }
} 
