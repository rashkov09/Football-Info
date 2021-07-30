package softuni.exam.service.imp;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.jsonDtos.PlayerSeedDto;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.service.PlayerService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.util.Arrays;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final static String PLAYERS_FILE_PATH = "src/main/resources/files/json/players.json";
    private final PlayerRepository playerRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final FileUtil fileUtil;

    public PlayerServiceImpl(PlayerRepository playerRepository, Gson gson, ModelMapper modelMapper, ValidatorUtil validatorUtil, FileUtil fileUtil) {
        this.playerRepository = playerRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.fileUtil = fileUtil;
    }


    @Override
    public String importPlayers() throws IOException {
        PlayerSeedDto[] playerSeedDtos = gson.fromJson(fileUtil.readFile(PLAYERS_FILE_PATH), PlayerSeedDto[].class);
        Arrays.stream(playerSeedDtos).forEach(playerSeedDto -> {
            boolean valid = validatorUtil.isValid(playerSeedDto);
            //TODO
        });
        return "";
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() >0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return fileUtil.readFile(PLAYERS_FILE_PATH);
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        //TODO Implement me
        return "";
    }

    @Override
    public String exportPlayersInATeam() {
        //TODO Implement me
        return "";
    }
}
