package fontRendering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import renderEngine.Loader;

public class TextMaster {

	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	

}
