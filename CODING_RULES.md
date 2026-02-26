# Règles de Codage - Architecture Monolithe

Ce document définit les règles et conventions de codage à suivre pour maintenir la cohérence, la lisibilité et la qualité du code au sein de ce projet. Ces règles sont basées sur l'architecture modulaire établie dans les paquets `base`.

## 1. Architecture Générale

Le projet suit une architecture en couches classique :

1.  **Entity** : Représentation de la donnée en base.
2.  **Repository** : Couche d'accès aux données (Data Access Layer) via Spring Data JPA.
3.  **DTO (Data Transfer Object)** : Objets simples pour transférer les données entre les couches, notamment vers l'extérieur (API).
4.  **Mapper** : Utilitaire de conversion entre les `Entity` et les `DTO`.
5.  **Service** : Couche métier qui orchestre la logique applicative.
6.  **Controller** : Couche d'exposition des API REST.

Chaque composant doit hériter des classes et interfaces de base pour garantir l'uniformité et la réutilisation du code.

---

## 2. Entités (Entities)

Toute entité de la base de données doit hériter de `SuperEntity`.

- **Héritage** : `public class MyEntity extends SuperEntity { ... }`
- **Annotations Lombok** : Utiliser `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` pour réduire le code boilerplate.
- **Identifiant** : La méthode `getIdentifier()` doit être implémentée et simplement retourner l'ID.

**Exemple :**
```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends SuperEntity {
    private String username;
    // ... autres champs

    @Override
    @Transient
    public Long getIdentifier() {
        return this.getId();
    }
}
```

---

## 3. DTOs (Data Transfer Objects)

Tout DTO doit implémenter l'interface `SuperDto`. Il est fortement recommandé d'utiliser les `Record` Java pour leur concision et leur immuabilité.

- **Implémentation** : `public record MyDto(...) implements SuperDto { ... }`
- **Identifiant** : La méthode `getIdentifier()` doit être implémentée. Pour un `record`, cela se fait directement dans le corps.

**Exemple :**
```java
public record UserDto(Long id, String username, List<String> roles) implements SuperDto {
    @Override
    public Long getIdentifier() {
        return id;
    }
}
```

---

## 4. Dépôts (Repositories)

Toute interface de dépôt doit hériter de `SuperRepository<EntityType>`.

- **Héritage** : `public interface MyRepository extends SuperRepository<MyEntity> { ... }`
- **Annotations** : L'annotation `@Repository` est optionnelle si vous héritez d'une interface Spring Data.

**Exemple :**
```java
public interface UserRepository extends SuperRepository<User> {
    // Méthodes de recherche personnalisées ici
    Optional<User> findByUsername(String username);
}
```

---

## 5. Mappeurs (Mappers)

Toute interface de mapping doit hériter de `SuperMapper<DtoType, EntityType>` et utiliser MapStruct.

- **Héritage** : `public interface MyMapper extends SuperMapper<MyDto, MyEntity> { ... }`
- **Annotation** : Doit être annotée avec `@Mapper(componentModel = "spring")`.

**Exemple :**
```java
@Mapper(componentModel = "spring")
public interface UserMapper extends SuperMapper<UserDto, User> {
    // MapStruct générera les implémentations pour fromRecord et toRecord.
    // Des mappings personnalisés peuvent être ajoutés ici si nécessaire.
}
```

---

## 6. Services

Toute classe de service pour des opérations CRUD de base doit hériter de `SuperService`.

- **Héritage** : `public class MyService extends SuperService<MyEntity, MyDto, MyRepository, MyMapper> { ... }`
- **Injection de dépendances** : Les dépendances (`repository`, `mapper`) sont injectées via le constructeur de `SuperService` et doivent être déclarées `final`.
- **Logique métier** : Les méthodes publiques doivent manipuler et retourner des `DTOs`, pas des `Entities`, pour respecter l'encapsulation des couches.

**Exemple :**
```java
@Service
public class UserService extends SuperService<User, UserDto, UserRepository, UserMapper> {

    public UserService(UserRepository repository, UserMapper mapper) {
        super(repository, mapper);
    }

    // Implémenter ici la logique métier spécifique à l'utilisateur
    public UserDto findByUsername(String username) {
        User user = repository.findByUsername(username).orElseThrow();
        return mapper.toRecord(user);
    }
}
```

---

## 7. Contrôleurs (Rest Controllers)

Toute classe de contrôleur REST doit hériter de `SuperRestController`.

- **Héritage** : `public class MyRestController extends SuperRestController<MyDto, MyService> { ... }`
- **Annotations** : Utiliser `@RestController` et `@RequestMapping` pour définir le point d'entrée de l'API.
- **Injection de dépendances** : Le service est injecté via le constructeur de `SuperRestController`.

**Exemple :**
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController extends SuperRestController<UserDto, UserService> {

    public UserRestController(UserService service) {
        super(service);
    }

    // Les endpoints CRUD (findAll, findById, save, update, delete) sont déjà fournis par SuperRestController.
    // Vous pouvez les surcharger si un comportement spécifique est nécessaire.

    // Ajouter ici des endpoints personnalisés
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(service.findByUsername(username));
    }
}
```

---

## 8. Conventions Générales

### Documentation (Javadoc)
- Toute classe, interface et méthode publique **doit** être documentée avec de la Javadoc.
- Utiliser la syntaxe Markdown (apostrophes inverses `` ` ``) pour mettre en évidence les noms de classes, de méthodes ou de variables.
  - **Exemple** : `/** Trouve un `UserDto` par son `id`. */`

### Style de Code
- **Immuabilité** : Préférer les objets immuables (comme les `records` pour les DTOs) et les collections immuables (`List.of()`, `Collectors.toUnmodifiableList()`).
- **Streams** : Utiliser l'API Stream de Java 8+ pour le traitement des collections.
- **Optional** : Gérer les `Optional` retournés par les repositories de manière explicite (`orElseThrow`, `orElse`, `ifPresent`, etc.). Ne pas appeler `.get()` sans vérification.