# OOP Principles Examples in SpringMVC-LaptopShop

## 1. ABSTRACTION

**Definition**: Hiding implementation details and showing only essential features through interfaces and abstract classes.

### Example 1: UserDetailsService Interface
**Location**: `CustomUserDetailsService.java` implements `UserDetailsService`

```java
// Spring Security's interface (abstraction)
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}

// Your implementation (concrete)
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Implementation details hidden from callers
        vn.hoidanit.laptopshop.domain.User user = this.userService.getUserByEmail(username);
        // ... implementation
    }
}
```
**Why it's abstraction**: Callers only know the interface contract (`loadUserByUsername`), not the implementation details (how you fetch users, what database you use, etc.).

### Example 2: ConstraintValidator Interface
**Location**: `RegisterValidator.java` implements `ConstraintValidator`

```java
// Jakarta Validation interface (abstraction)
public interface ConstraintValidator<A extends Annotation, T> {
    void initialize(A constraintAnnotation);
    boolean isValid(T value, ConstraintValidatorContext context);
}

// Your implementation
@Service
public class RegisterValidator implements ConstraintValidator<RegisterChecked, RegisterDTO> {
    @Override
    public boolean isValid(RegisterDTO user, ConstraintValidatorContext context) {
        // Implementation: check password match, email exists, etc.
    }
}
```
**Why it's abstraction**: The validation framework only knows the interface, not how you validate (database checks, password matching logic, etc.).

### Example 3: AuthenticationSuccessHandler Interface
**Location**: `CustomSuccessHandler.java` implements `AuthenticationSuccessHandler`

```java
// Spring Security interface (abstraction)
public interface AuthenticationSuccessHandler {
    void onAuthenticationSuccess(
        HttpServletRequest request, 
        HttpServletResponse response, 
        Authentication authentication
    ) throws IOException, ServletException;
}

// Your implementation
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(...) {
        // Your custom logic: set session attributes, redirect based on role
    }
}
```
**Why it's abstraction**: Spring Security calls your handler through the interface without knowing your specific redirect logic or session management.

### Example 4: JpaRepository Interface
**Location**: All repository interfaces extend `JpaRepository`

```java
// Spring Data JPA interface (abstraction)
public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
    // Methods like save(), findAll(), deleteById(), etc. are abstract
}

// Your concrete repositories
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);  // You only declare, Spring implements
}
```
**Why it's abstraction**: You declare methods, but Spring Data JPA provides the implementation. You don't write SQL or database connection code.

---

## 2. ENCAPSULATION

**Definition**: Bundling data and methods together, hiding internal state and providing controlled access through getters/setters.

### Example 1: User Entity
**Location**: `domain/User.java`

```java
@Entity
@Table(name = "users")
public class User {
    // Private fields - data is encapsulated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;  // Private - cannot be accessed directly
    
    @NotEmpty
    @Email
    private String email;  // Private
    
    @NotNull
    @Size(min = 2)
    private String password;  // Private
    
    private String fullName;  // Private
    
    // Public getters/setters - controlled access
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;  // Can add validation here
    }
    
    public String getPassword() {
        return password;  // Could hash before returning
    }
    
    public void setPassword(String password) {
        this.password = password;  // Could hash before storing
    }
    
    // Other getters/setters...
}
```
**Why it's encapsulation**: 
- Fields are `private` - cannot be accessed directly from outside
- Access only through `public` getters/setters
- Can add validation, transformation, or security in getters/setters
- Internal structure can change without affecting code that uses the class

### Example 2: Product Entity
**Location**: `domain/Product.java`

```java
@Entity
@Table(name = "products")
public class Product {
    private long id;  // Encapsulated
    private String name;  // Encapsulated
    private double price;  // Encapsulated
    private long quantity;  // Encapsulated
    
    // Controlled access through getters/setters
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        // Could add validation: if (price < 0) throw exception
        this.price = price;
    }
}
```

### Example 3: Service Classes with Private Dependencies
**Location**: `services/UserService.java`, `services/ProductService.java`

```java
@Service
public class UserService {
    // Private dependencies - encapsulated
    private final UserRepository userRepository;  // Private
    private final RoleRepository roleRepository;  // Private
    
    // Constructor injection - dependencies are set internally
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;  // Encapsulated
        this.roleRepository = roleRepository;  // Encapsulated
    }
    
    // Public methods - controlled access to functionality
    public User getUserByEmail(String email) {
        // Internal implementation uses private repositories
        return this.userRepository.findByEmail(email);
    }
}
```
**Why it's encapsulation**: 
- Dependencies are `private` - cannot be accessed or modified from outside
- Internal implementation (which repository, how data is fetched) is hidden
- Public methods provide controlled interface to the service's functionality

---

## 3. INHERITANCE

**Definition**: A class/interface inheriting properties and methods from a parent class/interface.

### Example 1: Repository Interfaces Extending JpaRepository
**Location**: All repository interfaces

```java
// Parent interface (from Spring Data JPA)
public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
    <S extends T> S save(S entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    void deleteById(ID id);
    long count();
    // ... many more methods
}

// Child interface - inherits all methods from JpaRepository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Inherits: save(), findAll(), findById(), deleteById(), count(), etc.
    // Adds custom methods
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
```
**Why it's inheritance**: 
- `UserRepository` **inherits** all methods from `JpaRepository`
- You can call `userRepository.save()`, `userRepository.findAll()` without defining them
- You can add your own methods (`findByEmail`) on top of inherited ones

### Example 2: ProductRepository
**Location**: `repository/ProductRepository.java`

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Inherits all JpaRepository methods
    // Adds custom method with pagination
    Page<Product> findAll(Pageable pageable);
}
```

### Example 3: OrderRepository, CartRepository, etc.
**Location**: Multiple repository files

```java
// All these inherit from JpaRepository
public interface OrderRepository extends JpaRepository<Order, Long> {}
public interface CartRepository extends JpaRepository<Cart, Long> {}
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {}
```
**Why it's inheritance**: All repositories inherit common CRUD operations from `JpaRepository`, avoiding code duplication.

### Example 4: Implementing Interfaces (Interface Inheritance)
**Location**: `CustomUserDetailsService.java`, `CustomSuccessHandler.java`

```java
// Interface defines contract
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}

// Class inherits the contract (must implement all methods)
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override  // Must implement inherited method
    public UserDetails loadUserByUsername(String username) {
        // Implementation
    }
}
```
**Why it's inheritance**: The class "inherits" the contract/interface from `UserDetailsService` and must provide implementations.

---

## 4. POLYMORPHISM

**Definition**: The ability of objects of different types to be accessed through the same interface, with behavior specific to each type.

### Example 1: UserDetailsService Polymorphism
**Location**: `CustomUserDetailsService.java` and `SecurityConfiguration.java`

```java
// Interface type
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}

// Implementation 1: Your custom implementation
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Your logic: fetch from database
    }
}

// Usage - accepts any UserDetailsService implementation
@Bean
public UserDetailsService userDetailsService(UserService userService) {
    return new CustomUserDetailsService(userService);  // Returns interface type
}

// Spring Security uses it polymorphically
@Bean
public DaoAuthenticationProvider authProvider(
    UserDetailsService userDetailsService  // Accepts any implementation
) {
    authProvider.setUserDetailsService(userDetailsService);  // Works with any implementation
}
```
**Why it's polymorphism**: 
- `DaoAuthenticationProvider` accepts `UserDetailsService` interface
- It doesn't care if it's `CustomUserDetailsService` or any other implementation
- You could swap implementations without changing `DaoAuthenticationProvider` code
- Same interface, different behaviors

### Example 2: AuthenticationSuccessHandler Polymorphism
**Location**: `CustomSuccessHandler.java` and `SecurityConfiguration.java`

```java
// Interface
public interface AuthenticationSuccessHandler {
    void onAuthenticationSuccess(...);
}

// Your implementation
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(...) {
        // Your custom logic: role-based redirect, session setup
    }
}

// Usage - Spring Security accepts any implementation
@Bean
public AuthenticationSuccessHandler customSuccessHandler() {
    return new CustomSuccessHandler();  // Returns interface type
}

// Used polymorphically
.formLogin(formLogin -> formLogin
    .successHandler(customSuccessHandler())  // Accepts any AuthenticationSuccessHandler
)
```
**Why it's polymorphism**: Spring Security's `formLogin()` accepts any `AuthenticationSuccessHandler` implementation. You could create different handlers (e.g., `AdminSuccessHandler`, `UserSuccessHandler`) and swap them.

### Example 3: ConstraintValidator Polymorphism
**Location**: `RegisterValidator.java`, `StrongPasswordValidator.java`

```java
// Interface
public interface ConstraintValidator<A, T> {
    boolean isValid(T value, ConstraintValidatorContext context);
}

// Implementation 1: RegisterValidator
public class RegisterValidator implements ConstraintValidator<RegisterChecked, RegisterDTO> {
    @Override
    public boolean isValid(RegisterDTO user, ...) {
        // Validates: password match, email exists
    }
}

// Implementation 2: StrongPasswordValidator
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    @Override
    public boolean isValid(String password, ...) {
        // Validates: password strength
    }
}

// Usage - Jakarta Validation framework uses them polymorphically
@RegisterChecked  // Uses RegisterValidator
public class RegisterDTO { ... }

@StrongPassword  // Uses StrongPasswordValidator
private String password;
```
**Why it's polymorphism**: The validation framework treats all `ConstraintValidator` implementations the same way, calling `isValid()` regardless of which specific validator it is.

### Example 4: Repository Polymorphism
**Location**: All repository interfaces

```java
// All repositories implement JpaRepository contract
UserRepository extends JpaRepository<User, Long>
ProductRepository extends JpaRepository<Product, Long>
OrderRepository extends JpaRepository<Order, Long>

// Spring Data JPA provides implementations at runtime
// You can use them polymorphically:

// In a generic service (hypothetical)
public <T, ID> void saveEntity(JpaRepository<T, ID> repository, T entity) {
    repository.save(entity);  // Works with any repository type
}

// Or Spring's internal code:
// Spring creates proxy implementations for all repositories
// All follow the same JpaRepository interface
// Same methods (save, findAll, etc.) work on all of them
```
**Why it's polymorphism**: All repositories share the same interface (`JpaRepository`), so code that works with one repository type can work with any repository type.

### Example 5: Method Overriding (Runtime Polymorphism)
**Location**: Multiple classes

```java
// Base interface
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}

// Override in implementation
public class CustomUserDetailsService implements UserDetailsService {
    @Override  // Polymorphic method - different behavior than other implementations
    public UserDetails loadUserByUsername(String username) {
        // Your specific implementation
    }
}

// Another implementation could have different behavior:
public class InMemoryUserDetailsService implements UserDetailsService {
    @Override  // Same method, different implementation
    public UserDetails loadUserByUsername(String username) {
        // Different implementation: loads from memory instead of database
    }
}
```
**Why it's polymorphism**: Same method name (`loadUserByUsername`), but different implementations provide different behaviors. The caller doesn't need to know which implementation is used.

---

## Summary Table

| Principle | Example | Location | Key Point |
|-----------|---------|----------|-----------|
| **Abstraction** | `UserDetailsService` interface | `CustomUserDetailsService.java` | Interface hides implementation details |
| **Abstraction** | `ConstraintValidator` interface | `RegisterValidator.java` | Validation framework doesn't know your validation logic |
| **Abstraction** | `JpaRepository` interface | All repositories | You declare methods, Spring implements them |
| **Encapsulation** | `User` entity with private fields | `domain/User.java` | Private fields, public getters/setters |
| **Encapsulation** | Service classes with private dependencies | `UserService.java` | Private repositories, public methods |
| **Inheritance** | Repositories extend `JpaRepository` | All repository interfaces | Inherit save(), findAll(), etc. |
| **Inheritance** | Classes implement interfaces | `CustomUserDetailsService` | Inherit interface contracts |
| **Polymorphism** | `UserDetailsService` implementations | `SecurityConfiguration.java` | Same interface, different implementations |
| **Polymorphism** | `ConstraintValidator` implementations | Multiple validators | Framework uses validators polymorphically |
| **Polymorphism** | Repository interfaces | All repositories | Same interface, different entity types |

