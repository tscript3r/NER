package pl.tscript3r.ner.config;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationFacade {

    public boolean isFirstStart() {
        return true;
    }

}
