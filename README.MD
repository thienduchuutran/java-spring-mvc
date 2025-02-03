1 user có 1 role  và table user chứa role_id (owner side)
User -> ManyToOne - lưu là Role
@ManyToOne
private Role role;


1 role có nhiều user ->   Role - OneToMany - lưu là List<User>
@OneToMany
List<User> users;
