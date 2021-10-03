package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Player;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {

    Set<Player> findPlayersByTeamName(String name);

    Set<Player> findAllBySalaryAfter(BigDecimal salary);
}
