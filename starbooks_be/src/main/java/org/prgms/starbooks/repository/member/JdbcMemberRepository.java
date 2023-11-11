package org.prgms.starbooks.repository.member;

import org.prgms.starbooks.exception.DatabaseException;
import org.prgms.starbooks.model.member.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

import static org.prgms.starbooks.utils.JdbcUtils.toLocalDateTime;
import static org.prgms.starbooks.utils.JdbcUtils.toUUID;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member insert(Member member) {
        int update = jdbcTemplate.update("INSERT INTO members(member_id, name, email, address, postcode, created_at, updated_at) " +
                        "VALUES(UUID_TO_BIN(:memberId), :name, :email, :address, :postcode, :createdAt, :updatedAt)",
                toParamMap(member));
        if (update != 1) {
            throw new DatabaseException("회원 insert 중 에러가 발생했습니다.");
        }
        return member;
    }

    @Override
    public Member update(Member member) {
        int update = jdbcTemplate.update("UPDATE members SET address = :address, postcode = :postcode, updated_at = :updatedAt " +
                        "WHERE member_id = UUID_TO_BIN(:memberId)",
                toParamMap(member));
        if (update != 1) {
            throw new DatabaseException("회원 update 중 에러가 발생했습니다.");
        }
        return member;
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("SELECT * FROM members", memberRowMapper);
    }

    @Override
    public Optional<Member> findById(UUID memberId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM members WHERE member_id = UUID_TO_BIN(:memberId)",
                            Collections.singletonMap("memberId", memberId.toString().getBytes()),
                            memberRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM members WHERE email = :email",
                            Collections.singletonMap("email", email),
                            memberRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM members", Collections.emptyMap());
    }

    @Override
    public void deleteById(UUID memberId) {
        jdbcTemplate.update("DELETE FROM members WHERE member_id = UUID_TO_BIN(:memberId)",
                Collections.singletonMap("memberId", memberId.toString().getBytes()));
    }

    private static final RowMapper<Member> memberRowMapper = (resultSet, i) -> {
        UUID memberId = toUUID(resultSet.getBytes("member_id"));
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String address = resultSet.getString("address");
        String postcode = resultSet.getString("postcode");
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        return new Member(memberId, name, email, address, postcode, createdAt, updatedAt);
    };

    private Map<String, Object> toParamMap(Member member) {
        Map<String, Object> param = new HashMap<>();
        param.put("memberId", member.getMemberId().toString().getBytes());
        param.put("name", member.getName());
        param.put("email", member.getEmail());
        param.put("address", member.getAddress());
        param.put("postcode", member.getPostcode());
        param.put("createdAt", member.getCreatedAt());
        param.put("updatedAt", member.getUpdatedAt());
        return param;
    }
}
