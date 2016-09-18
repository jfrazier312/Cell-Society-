package config;

public class Configuration {
	
	private ModelConfiguration modelConfig;
	private ControllerConfiguration ctrlrConfig;
	private ViewConfiguration viewConfig;
	
	public ModelConfiguration getModelConfig() {
		return modelConfig;
	}
	
	public Configuration setModelConfig(ModelConfiguration modelConfig) {
		this.modelConfig = modelConfig;
		return this;
	}
	
	public ControllerConfiguration getCtrlrConfig() {
		return ctrlrConfig;
	}
	
	public Configuration setCtrlrConfig(ControllerConfiguration ctrlrConfig) {
		this.ctrlrConfig = ctrlrConfig;
		return this;
	}
	
	public ViewConfiguration getViewConfig() {
		return viewConfig;
	}
	
	public Configuration setViewConfig(ViewConfiguration viewConfig) {
		this.viewConfig = viewConfig;
		return this;
	}
}
