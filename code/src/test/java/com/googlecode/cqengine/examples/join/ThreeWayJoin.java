/**
 * Copyright 2012-2015 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.examples.join;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * Demonstrates a JOIN between three IndexedCollections.
 * <p/>
 * Given:
 * a collection of Users (having a userId + userName),
 * a collections of Roles (having roleId + roleName),
 * and a collection of UserRoles (having userId + roleId),
 * ...find users who have the roleName "Manager".
 *
 * @author npgall
 */
public class ThreeWayJoin {

    public static void main(String[] args) {
        IndexedCollection<User> users = new ConcurrentIndexedCollection<User>();
        users.add(new User(1, "Joe"));
        users.add(new User(2, "Jane"));
        users.add(new User(3, "Jesse"));

        IndexedCollection<Role> roles = new ConcurrentIndexedCollection<Role>();
        roles.add(new Role(1, "CEO"));
        roles.add(new Role(2, "Manager"));
        roles.add(new Role(3, "Employee"));

        IndexedCollection<UserRole> userRoles = new ConcurrentIndexedCollection<UserRole>();
        userRoles.add(new UserRole(1, 3)); // Joe is an Employee
        userRoles.add(new UserRole(2, 2)); // Jane is a Manager
        userRoles.add(new UserRole(3, 2)); // Jesse is a Manager

        // Retrieve Users who are managers...
        Query<User> usersWhoAreManagers =
                existsIn(userRoles, User.USER_ID, UserRole.USER_ID,
                        existsIn(roles, UserRole.ROLE_ID, Role.ROLE_ID, equal(Role.ROLE_NAME, "Manager")));

        for (User u : users.retrieve(usersWhoAreManagers)) {
            System.out.println(u.userName);
        }
        // ..prints: Jane, Jesse
    }

    // === Domain objects ===

    static class User {
        final int userId;
        final String userName;

        public User(int userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }

        static final Attribute<User, Integer> USER_ID = new SimpleAttribute<User, Integer>() {
            public Integer getValue(User user, QueryOptions queryOptions) { return user.userId; }
        };

        static final Attribute<User, String> USER_NAME = new SimpleAttribute<User, String>() {
            public String getValue(User user, QueryOptions queryOptions) { return user.userName; }
        };
    }

    static class Role {
        final int roleId;
        final String roleName;

        public Role(int roleId, String roleName) {
            this.roleId = roleId;
            this.roleName = roleName;
        }

        static final Attribute<Role, Integer> ROLE_ID = new SimpleAttribute<Role, Integer>() {
            public Integer getValue(Role role, QueryOptions queryOptions) { return role.roleId; }
        };
        static final Attribute<Role, String> ROLE_NAME = new SimpleAttribute<Role, String>() {
            public String getValue(Role role, QueryOptions queryOptions) { return role.roleName; }
        };
    }

    static class UserRole {
        final int userId;
        final int roleId;

        public UserRole(int userId, int roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }

        static final Attribute<UserRole, Integer> USER_ID = new SimpleAttribute<UserRole, Integer>() {
            public Integer getValue(UserRole userRole, QueryOptions queryOptions) { return userRole.userId; }
        };

        static final Attribute<UserRole, Integer> ROLE_ID = new SimpleAttribute<UserRole, Integer>() {
            public Integer getValue(UserRole userRole, QueryOptions queryOptions) { return userRole.roleId; }
        };
    }
}
