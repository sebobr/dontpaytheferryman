package net.opentsdb.kafka.consumer.modules

import com.google.inject.{Singleton, Provides, AbstractModule}
import net.codingwell.scalaguice.ScalaModule
import java.util.Properties
import com.google.inject.name.{Named, Names}
import java.util.concurrent.{LinkedBlockingQueue, ArrayBlockingQueue}
import org.hbase.async.HBaseClient
import net.opentsdb.core.TSDB
import net.opentsdb.utils.Config

class ConsumerModule(props: Properties, config: Config) extends AbstractModule with ScalaModule {
  def configure() {
    Names.bindProperties(binder(), props)
  }

  @Provides @Singleton def provideProperties: Properties = props

  @Provides @Singleton def provideHBaseClient(@Named("hbase.zookeeper") zk: String): HBaseClient = {
    val client = new HBaseClient(zk)

    client.setFlushInterval(500)

    client
  }

  @Provides @Singleton def provideTsdbClient(config:Config, client: HBaseClient): TSDB = new TSDB(client, config)

}
