package getl.config

import getl.h2.H2Connection
import getl.utils.Config

class ConfigTypesafeTest extends getl.test.GetlTest {
	@Override
	void setUp() {
		super.setUp()
		Config.configClassManager = new ConfigTypesafe(path: "typesafe/application.conf")
        Config.LoadConfig()
	}

	void testDbConnection() {
		H2Connection h2Connection = new H2Connection(config: 'h2')
		assertEquals 'jdbc:h2:mem:test_mem', h2Connection.connectURL
	}

	void testConfig() {
		assertTrue Config.ContainsSection('connections')
	}

	void testVariable() {
		assertEquals 'test', Config.content.some_var
	}
}
