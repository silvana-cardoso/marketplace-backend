package com.ericsson.rumpup.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.java.marketplace.dto.UserInsertDTO;
import com.java.marketplace.entities.User;
import com.java.marketplace.repositories.UserRepository;
import com.java.marketplace.services.RoleService;
import com.java.marketplace.services.UserService;
import com.java.marketplace.services.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    public static final Long ID = 1L;
    public static final String EMAIL = "user_teste_mail.com";
    public static final String PASSWORD = "123456";
    public static final int INDEX = 0;
    //criar instância da classe a ser testada
    // e injeta os mocks nela
    @InjectMocks
    private UserService service;

    @Mock
    private RoleService roleService;
    @Mock
    private UserRepository repository;

    private User user;
//    private UserDTO userDTO;
    private UserInsertDTO userInsertDTO;
    private Optional<User> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();

    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void whenFindAllThenReturnAListOfUsers() {
        Pageable pageable = PageRequest.of(INDEX,10);
        List<User> userList = List.of(user);
        Page<User> userPage = new PageImpl<>(userList,pageable,userList.size());

        when(repository.findAll(pageable)).thenReturn(userPage);

        Page<User> response = service.findAll(pageable);

        assertNotNull(response);
        assertEquals(10,response.getSize());
        assertEquals(User.class,response.getContent().get(INDEX).getClass());

        assertEquals(ID, response.getContent().get(INDEX).getId());
        assertEquals(EMAIL, response.getContent().get(INDEX).getEmail());

    }

    @Test
    void findByEmail() {
    }

    @Test
    void whenFindByIdWithValidIdThenReturnUserInstance() {
        when(repository.findById(anyLong())).thenReturn(optionalUser);
        User response = service.findById(ID);

        assertNotNull(response);
        assertEquals(User.class,response.getClass());
        assertEquals(ID,response.getId());
        assertEquals(EMAIL,response.getEmail());
    }

    @Test
    void whenFindByIdWithInvalidIdReturnAnResourceNotFoundException() {
        when(repository.findById(anyLong())).thenThrow(new ResourceNotFoundException(User.class,ID));

        try {
            service.findById(ID);
        } catch (Exception ex) {
            assertEquals(ResourceNotFoundException.class,ex.getClass());
            assertEquals(User.class.getSimpleName() + " not found with id: " + ID, ex.getMessage());
        }

    }

    @Test
    void getReferenceById() {
    }

    @Test
    void whenInsertWithValidEmailThenReturnSuccess() {
        when(repository.save(any())).thenReturn(user);

        User response = service.insert(userInsertDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(EMAIL, response.getEmail());

    }

    @Test
    void insertAdmin() {
    }

    @Test
    void delete() {
    }

    @Test
    void whenUpdateWithValidUserInsertDTOThenReturnSuccess() {
    	when(repository.getReferenceById(anyLong())).thenReturn(user);
        when(repository.save(any())).thenReturn(user);

        User response = service.update(ID, userInsertDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(EMAIL, response.getEmail());

    }
    
    @Test
    void whenUpdateWithValidDeletedUserThenReturnAnResourceNotFoundException() {
    	when(repository.getReferenceById(anyLong())).thenReturn(user);
        when(repository.save(any())).thenReturn(user);

        User response = service.update(ID, userInsertDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(EMAIL, response.getEmail());

    }

    @Test
    void partialUpdate() {
    }

    private void startUser() {
        user = new User(ID, EMAIL, PASSWORD);
//        userDTO = new UserDTO(user);
        userInsertDTO = new UserInsertDTO(EMAIL,PASSWORD);
        optionalUser = Optional.of(new User(ID, EMAIL, PASSWORD));

    }
}