package org.neo4j.boot.test;

import com.graphaware.common.policy.inclusion.NodeInclusionPolicy;
import com.graphaware.module.timetree.domain.Resolution;
import com.graphaware.module.timetree.module.TimeTreeConfiguration;
import com.graphaware.module.timetree.module.TimeTreeModule;
import com.graphaware.runtime.GraphAwareRuntime;
import com.graphaware.runtime.GraphAwareRuntimeFactory;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.boot.test.domain.User;
import org.neo4j.boot.test.repository.UserRepository;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import java.io.File;
import java.util.TimeZone;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootExampleApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(SpringBootExampleApplicationTests.class);
    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    @Autowired
    private GraphDatabaseService graphDb;

    @Autowired
    private GraphAwareRuntime runtime;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
	public void test_create_user() throws Exception {
        mockMvc.perform(post("/test")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"aa@bb.com\"}"))
            .andExpect(status().isOk())
            .andExpect(content().json("{\n" +
                "  \"firstName\": null,\n" +
                "  \"lastName\": null,\n" +
                "  \"email\": \"aa@bb.com\"\n" +
                "}"));
    }

    @Test
    public void testTimeTree() {
        userRepository.save(new User("alper@alper.com", "alper", "alper"));
        Iterable<User> all = userRepository.findAll();
        System.out.println("there are "+  all);
        for(User  user : all ) {

            System.out.println("found user: " + user.getEmail());
        }

    }
}
