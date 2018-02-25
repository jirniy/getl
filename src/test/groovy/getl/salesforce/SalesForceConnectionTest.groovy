package getl.salesforce

import getl.csv.CSVConnection
import getl.csv.CSVDataset
import getl.proc.Flow
import getl.stat.ProcessTime
import getl.utils.Config
import getl.utils.FileUtils
import getl.utils.Logs

class SalesForceConnectionTest extends GroovyTestCase {
	static final def configName = 'tests/salesforce/config.json'
	private SalesForceConnection connection

	void setUp() {
		if (!FileUtils.ExistsFile(configName)) return
		Config.LoadConfig(configName)
		Logs.Init()

		connection = new SalesForceConnection(config: 'salesforce')
	}

	void testConnect() {
		if (connection == null) return
		connection.connected = true
		assertTrue(connection.connected)
	}

	void testRetrieveObjects() {
		if (connection == null) return
		assertTrue(connection.retrieveObjects().find { it.objectName == 'Account' } != null)
	}

	void testGetFields() {
		if (connection == null) return
		SalesForceDataset dataset = new SalesForceDataset(connection: connection, sfObjectName: 'Account')
		dataset.retrieveFields()

		assertTrue(dataset.field.find { it.name == 'Id' } != null)
	}

	void testRows() {
		if (connection == null) return
		SalesForceDataset dataset = new SalesForceDataset(connection: connection, sfObjectName: 'Account')
		assertTrue(dataset.rows(limit: 10).size() == 10)
	}

    void testFlowCopy() {
        if (connection == null) return
        SalesForceDataset source = new SalesForceDataset(connection: connection, sfObjectName: 'Account')

        CSVConnection csvConnection = new CSVConnection(path: 'tests/salesforce')
        CSVDataset dest = new CSVDataset(connection: csvConnection, fileName: 'test.csv')

        source.retrieveFields()
        source.field.removeAll { !(it.name in ['Id', 'Name']) }
        dest.field = source.field

        def pt = new ProcessTime(name: 'Copy data from SalesForce')
        def count = new Flow().copy(source: source, dest: dest, source_limit: 100)
        pt.finish(count)
    }

	void testDisconnect() {
		if (connection == null) return
		connection.connected = false
		assertFalse(connection.connected)
	}
}
