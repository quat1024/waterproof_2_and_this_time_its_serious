package quaternary.waterproof2.animationstatemachine;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name("Waterproof2 Loading Plugin")
@IFMLLoadingPlugin.TransformerExclusions("quaternary.waterproof2.animationstatemachine")
@IFMLLoadingPlugin.SortingIndex(1337)
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class Waterproof2LoadingPlugin implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"quaternary.waterproof2.animationstatemachine.Waterproof2ClassTransformer"};
	}
	
	@Override
	public String getModContainerClass() {
		return null;
	}
	
	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		
	}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
