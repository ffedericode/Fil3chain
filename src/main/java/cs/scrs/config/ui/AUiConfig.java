package cs.scrs.config.ui;

import java.util.List;
/**
 * Classe astratta che rappresenta la configurazione dell'ambiente utilizzato
 * @author ivan18
 *
 */
public abstract class AUiConfig {
	public abstract String getPrefix();
	public abstract String getSuffix();
	public abstract List<Resource> getResources();
}
