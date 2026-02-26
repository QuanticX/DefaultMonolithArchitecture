package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.controller.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.business.UserService;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.controller.rest.base.SuperRestController;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.dto.UserDto;

/**
 * REST controller for managing User entities.
 * <p>
 * This controller provides endpoints for user-related operations such as login verification,
 * retrieving users by name, and checking for user existence.
 * It extends the generic SuperRestController to inherit standard RESTful behavior.
 * </p>
 *
 * @RestController indicates that this class is a RESTful web service controller.
 * @RequestMapping("/api/v1/users") maps HTTP requests to /api/v1/users to this controller.
 */
@RestController
@RequestMapping(UserRestController.BASE_PATH)
public class UserRestController extends SuperRestController<UserDto, UserService> {


    /**
     * The base path for this controller.
     */
    public static final String BASE_PATH = "/api/v1/users";
    public static final String BASE_PATH_VERIFY_LOGIN = "/verify";
    public static final String BASE_PATH_GET_BY_NOM = "/get-by-nom";
    public static final String BASE_PATH_EXIST_BY_NOM = "/exist-by-nom";

    /**
     * Constructs a UserRestController with the specified UserService.
     *
     * @param service the UserService used to handle business logic for User entities
     */
    @Autowired
    protected UserRestController(UserService service) {
        super(service);
    }


    /**
     * Verifies the login credentials of a user.
     *
     * @param dto the UserDto containing login credentials
     * @return true if credentials are valid, false otherwise
     */
    @PostMapping(BASE_PATH_VERIFY_LOGIN)
    public Boolean verifyLogin(@RequestBody UserDto dto) {
        return service.verifyLogin(dto);
    }

    /**
     * Retrieves a user by their name.
     *
     * @param nom the name of the user
     * @return the UserDto corresponding to the given name
     */
    @GetMapping(BASE_PATH_GET_BY_NOM)
    public UserDto getUserByNom(@RequestParam("nom") String nom) {
        return service.findByNom(nom);
    }

    /**
     * Checks if a user exists by their name.
     *
     * @param nom the name of the user
     * @return true if a user with the given name exists, false otherwise
     */
    @GetMapping(BASE_PATH_EXIST_BY_NOM)
    public Boolean existByNom(@RequestParam("nom") String nom) {
        return service.existByNom(nom);
    }


}