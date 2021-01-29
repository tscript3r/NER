package pl.tscript3r.ner.migrate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tscript3r.ner.client.ClientFacade;
import pl.tscript3r.ner.migrate.utils.Progress;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class MigrateFacade {

    private final ClientFacade clientFacade;
    private Migrate migrate;

    public void connect(String file, Progress progress) throws SQLException {
        migrate = Migrate.connect(file, progress);
        migrate.init();
    }

    public void migrateClients() throws SQLException {
        migrate.migrateClients(clientFacade::save);
    }

}
