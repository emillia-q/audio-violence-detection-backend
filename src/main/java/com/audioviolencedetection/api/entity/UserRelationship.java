package com.audioviolencedetection.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_relationships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationship {
}
