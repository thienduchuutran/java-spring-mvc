1 user có 1 role  và table user chứa role_id (owner side)
User -> ManyToOne - lưu là Role
@ManyToOne
private Role role;


1 role có nhiều user ->   Role - OneToMany - lưu là List<User>
@OneToMany
List<User> users;

column mapping

Sử dụng mappedBy cho inverse side:
@OneToMany( mappedBy = “role”)
List<User> users;

Sử dụng joinColumns cho owner side:
@ManyToOne
@JoinColumn(name = "role_id")
private Role role;
