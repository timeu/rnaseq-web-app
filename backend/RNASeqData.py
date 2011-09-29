'''
Created on Sep 16, 2011

@author: uemit.seren
'''
'''
Created on Jun 17, 2011

@author: uemit.seren
'''

import tables
import csv
import time
from datetime import datetime
import pdb

'''
SQL Statement:
SELECT e.ecotypeid,e.nativename,e.longitude,e.latitude,e.country,CONCAT(e.firstname,' ', e.surname) as collector,e.collectiondate,t.dataset  INTO OUTFILE '/tmp/rnaseq_accessions.csv' from temp_rnaseq_accessions t LEFT JOIN stock.ecotype_info_with_haplogroup e ON e.ecotypeid = t.ecotype_id order by e.ecotypeid ASC;
'''

def importAccessions(hdf5_file,csv_file,delimiter='\t'):
    hdf5_f = None
    csv_f = None
    try:
        hdf5_f = tables.openFile(hdf5_file,"w")
        group = hdf5_f.createGroup("/", "accessions", "Accessions")
        csv_f =  csv.reader(open(csv_file,'rb'),delimiter=delimiter)
        table = hdf5_f.createTable(group,'infos',Accessions,'Accessions Infos')
        datasets = table.getEnum('dataset')
        for accession in csv_f:
            row = table.row
            row['accession_id'] = accession[0]
            row['name'] = accession[1]
            if accession[2] != "\\N":
                row['longitude'] = accession[2]
            if accession[3] != "\\N":
                row['latitude'] = accession[3]
            row['country'] = accession[4]
            row['collector'] = accession[5]
            if accession[6] != "\\N": 
                unix_time = int(time.mktime(datetime.strptime(accession[6],"%Y-%m-%d %H:%M:%S").timetuple()))
                print unix_time
            row['collection_date'] = unix_time
            row['dataset'] = datasets[accession[7]]
            row.append()
        table.flush()
    except Exception,err:
        print str(err)
    if hdf5_f is not None:
        hdf5_f.close()
    

class Accessions(tables.IsDescription):

    accession_id = tables.Int32Col()
    name = tables.StringCol(16)
    latitude = tables.Float32Col()
    longitude = tables.Float32Col()
    country = tables.StringCol(256)
    country_ISO = tables.StringCol(3)
    collector = tables.StringCol(256)
    collection_date = tables.Time32Col()
    dataset =  tables.EnumCol(['10','16','both'], '10', base='uint8')


