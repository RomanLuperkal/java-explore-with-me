package ru.practicum.server.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.server.user.dto.NewUserRequestResponse;
import ru.practicum.server.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query("SELECT new ru.practicum.server.user.dto.NewUserRequestResponse(u.userId, u.name, u.email) " +
            "FROM User AS u " +
            "WHERE u.userId IN :ids")
    List<NewUserRequestResponse> findUsers(Pageable pageable, @Param("ids") List<Long> ids);
}
