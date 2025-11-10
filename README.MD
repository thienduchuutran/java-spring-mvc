One user has one role, and the user table contains role_id (owner side).
User -> @ManyToOne — stored as Role

@ManyToOne
private Role role;


One role has many users → Role -> @OneToMany — stored as List<User>

@OneToMany
List<User> users;


Column mapping

Use mappedBy for the inverse side:

@OneToMany(mappedBy = "role")
List<User> users;


Use @JoinColumn for the owner side:

@ManyToOne
@JoinColumn(name = "role_id")
private Role role;
