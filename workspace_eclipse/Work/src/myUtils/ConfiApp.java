package myUtils;

import java.util.Iterator;
import java.util.Set;

import com.framework.gui.view.AppConfig;
import com.framework.gui.view.ConfigValue;
import com.logService.Logger;
import com.tcs.tmp.ConfigHandler;

public class ConfiApp {
	public static void main(String args[]) {
		ConfigHandler handler = new ConfigHandler();
		AppConfig conf = handler.loadConfig();
		if (conf.isEmpty()) {
			Logger.log("Loaded Confguration for FirstTime..");
		} else {
			Logger.log("Loaded Confguration for Second Time..");
			Set<String> keys = conf.keySet();
			Iterator<String> itr = keys.iterator();
			while (itr.hasNext()) {
				String str = itr.next();
				ConfigValue val = (ConfigValue) conf.getObject(str);
				System.out.println(str + " : " + val.getObject());
			}
			addConfig(handler, conf);
		}
	}

	/**
	 * @param handler
	 * @param conf
	 */
	private static void addConfig(ConfigHandler handler, AppConfig conf) {
		
		ConfigValue val0 = new ConfigValue(new Boolean(false));
		conf.putConfig("Application.License.check", val0);
		
		ConfigValue val1 = new ConfigValue(new Boolean(false));
		conf.putConfig("Application.License.check.fingerPrintVerificationRequired", val1);
		
		ConfigValue val2 = new ConfigValue(new Boolean(false));
		conf.putConfig("Application.update.support", val2);
		
		ConfigValue val3 = new ConfigValue(new Boolean(false));
		conf.putConfig("Application.update.required", val3);
		
		ConfigValue val4 = new ConfigValue(new String("1.0_a"));
		conf.putConfig("Application.version", val4);
		
		handler.saveConfiguration(conf);
	}

}
