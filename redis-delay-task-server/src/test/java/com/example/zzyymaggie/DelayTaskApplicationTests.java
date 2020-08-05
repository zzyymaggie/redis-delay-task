package com.example.zzyymaggie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DelayTaskApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testRequest() throws Exception {
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
		//存在
		mockMvc.perform(post("/delay/test")
                .content(requestBody)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)) //执行请求
				//打印请求和响应的详细信息
				.andDo(print())
		;
	}

}
