package com.mrsweeter.dreamhelper.Configuration;

import java.util.Map;

import com.mrsweeter.dreamhelper.DreamHelper;
import com.mrsweeter.dreamhelper.Language;

public class Loader {

	public static void loadAllConfig(Map<String, PluginConfiguration> configs)	{
		
		for (String str : configs.keySet()){
			configs.get(str).reload();
		}
	}
	
	public static void loadLanguage(PluginConfiguration lang)	{
		
		Language.noPerm = lang.getString("no-permission").replace(DreamHelper.color, "§");
		Language.suggestQuestion = lang.getString("suggest-question").replace(DreamHelper.color, "§");
		Language.answer = lang.getString("answer").replace(DreamHelper.color, "§");
		Language.submitConn = lang.getString("submit-to-connection").replace(DreamHelper.color, "§");
		Language.reload = lang.getString("reload").replace(DreamHelper.color, "§");
		Language.sendQuestion = lang.getString("send-question").replace(DreamHelper.color, "§");
		Language.clearAllSubmit = lang.getString("all-submit-clear").replace(DreamHelper.color, "§");
		Language.noSubmitNb = lang.getString("no-submit-X").replace(DreamHelper.color, "§");
		Language.submitXClear = lang.getString("submit-X-clear").replace(DreamHelper.color, "§");
		
	}
}
