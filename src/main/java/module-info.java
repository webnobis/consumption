module com.webnobis.consumption {

	requires org.slf4j;
	
	requires java.desktop;
	
	requires org.jfree.jfreechart;

	exports com.webnobis.consumption;
	exports com.webnobis.consumption.model;
	exports com.webnobis.consumption.repository.file.migrator.v1;

}
