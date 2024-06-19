
package org.example.backend.codeeditor.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; //기본키 id

    @Column(name="user_name", nullable = false)
    private String username; //사용자 이름

    //Constructors, getters, setters 정의

    public UserEntity() {}

    public UserEntity(String username){
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
