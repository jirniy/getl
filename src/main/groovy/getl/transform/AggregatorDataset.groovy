/*
 GETL - based package in Groovy, which automates the work of loading and transforming data. His name is an acronym for "Groovy ETL".

 GETL is a set of libraries of pre-built classes and objects that can be used to solve problems unpacking,
 transform and load data into programs written in Groovy, or Java, as well as from any software that supports
 the work with Java classes.
 
 Copyright (C) 2013-2015  Alexsey Konstantonov (ASCRUS)

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License and
 GNU Lesser General Public License along with this program.
 If not, see <http://www.gnu.org/licenses/>.
*/

package getl.transform

import groovy.transform.InheritConstructors
import getl.data.Connection
import getl.data.VirtualDataset
import getl.exception.ExceptionGETL

/**
 * Aggregation dataset class
 * @author Alexsey Konstantinov
 *
 */
@InheritConstructors
class AggregatorDataset extends VirtualDataset {
	AggregatorDataset () {
		super()
		
		sysParams.inheriteFields = true
		
		connection = new Connection(driver: AggregatorDatasetDriver)
		
		List<String> fieldByGroup = []
		params.fieldByGroup = fieldByGroup
		
		Map<String, Map> fieldCalc = [:]
		params.fieldCalc = fieldCalc
		
		params.algorithm = "HASH"
	}
	
	public List<String> getFieldByGroup () { params.fieldByGroup }
	public void setFieldByGroup (List<String> value) { params.fieldByGroup = value } 
	
	public Map<String, Map> getFieldCalc () { params.fieldCalc }
	public void setFieldCalc (Map<String, Map> value) { params.fieldCalc = value }
	
	public String getAlgorithm () { params.algorithm }
	public void setAlgorithm (String value) {
		value = value.toUpperCase()
		if (!(value in ["HASH", "TREE"])) throw new ExceptionGETL("Unknown algorithm \"${value}\"") 
		params.algorithm = value 
	}
}
