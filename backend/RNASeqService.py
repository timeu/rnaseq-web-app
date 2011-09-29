'''
Created on Sep 12, 2011

@author: uemit.seren
'''
import gviz_api, os
import tables
from datetime import datetime
from JBrowseDataSource import DataSource as JBrowseDataSource 
import cherrypy
import cPickle

class RNASeqService:
    base_path = "/net/gmi.oeaw.ac.at/gwasapp/rnaseq-web/"
    base_path_jbrowse = base_path
    base_path_datasets = base_path + "datasets/"
    base_jbrowse_path = base_path_jbrowse + "jbrowse_1.2.1/"
    track_folder = "TAIR10"
    
    #tracks/Chr%s/TAIR10/"
    __datasource = None
    _lazyArrayChunks = [{}, {}, {}, {}, {}]
    hdf5_filename = base_path + "rnaseq_data.hdf5"
    gene_annot_file = base_path + "genome_annotation.pickled"
    phenotype_file = base_path + "phenotypes_fake.pickled"
    
    def __init__(self):
        self.data_file = tables.openFile(self.hdf5_filename, "r")
        self.accessions = self._getAccessions()
        gene_annot_file = open(self.gene_annot_file, 'rb')
        self.gene_annotation = cPickle.load(gene_annot_file)
        gene_annot_file.close()
        phenotype_file = open(self.phenotype_file,'rb')
        self.phenotypes = cPickle.load(phenotype_file)
        phenotype_file.close()
            
    
    def _getLocationDistribution(self):
        import bisect, itertools
        from operator import itemgetter
        locations = {}
        selector = lambda row:row['country']
        for country, rows in itertools.groupby(sorted(self.accessions, key=itemgetter('country')), selector):
            locations[country] = len(list(rows))
        return locations
    
    def getLocationDistributionData(self):
        location_dist = self._getLocationDistribution()
        data = []
        for country, count in location_dist.iteritems():
            data.append({'Country':country, 'Count':count})
        column_name_type_ls = [("Country", ("string", "Country")), \
                          ("Count", ("number", "Count"))]
        column_ls = [row[0] for row in column_name_type_ls]
        data_table = gviz_api.DataTable(dict(column_name_type_ls))
        data_table.LoadData(data)
        return data_table.ToJSon(columns_order=column_ls)
    
    def _getAccessions(self):
        
        table = self.data_file.root.accessions.infos
        accessions = []
        datasets = table.getEnum('dataset')
        for row in table:
            collection_date = datetime.fromtimestamp(int(row['collection_date']))
            collection_date = datetime.strftime(collection_date, '%d.%m.%Y')
            accession = {'accession_id':row['accession_id'], 'collection_date':collection_date, 'collector':unicode(row['collector'], 'latin1'), 'country':row['country_ISO'], 'dataset':datasets.__call__(row['dataset']), 'latitude':row['latitude'], 'longitude':row['longitude'], 'name':unicode(row['name'], 'utf8')}
            accessions.append(accession)
        return accessions
    
    def _getPhenotypes(self, range_start,range_length,name='', chr='', start='', end=''):
        phenotypes_to_filter = []
        phenotypes_to_return = []
        stop = int(range_start)+int(range_length)
       
        isCriteria = False
        if (name != '') or (chr !='') or (start != '') or (end != ''):
            isCriteria = True
        if isCriteria == False:
            phenotypes_to_filter = self.phenotypes[:]
        else:
            i = 0
            for i in range(0,len(self.phenotypes)):
                phenotype = self.phenotypes[i]
                if (name =='' or name.lower() in unicode(phenotype['name'],'utf8').lower()) and \
                    (chr =='' or int(chr) == phenotype['chr']) and (start == '' or int(start) <= phenotype['start']) and \
                    (end == '' or int(end) >= phenotype['end']):
                    phenotypes_to_filter.append(phenotype)
        
        count = len(phenotypes_to_filter)
        if stop > count:
            range_start = range_start - stop
            stop = count
        if range_start < 0:
            range_start = 0
        for i in range(range_start,stop):
            phenotypes_to_return.append(phenotypes_to_filter[i])
        return phenotypes_to_return,count,range_start
    
    @cherrypy.expose
    def getAccessions(self):
        return {'accessions':self.accessions}
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getPhenotypes(self, range_start=0, range_length=1000, name='', chr='', start='', end=''):
        phenotypes, count,range_start = self._getPhenotypes(int(range_start), int(range_length), name, chr, start, end)
        return {'phenotypes':phenotypes, 'count':count, 'start':int(range_start),'length':int(range_length)}

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getGenes(self,chromosome,start,end,isFeatures=''):
        try:
            if self.__datasource == None:
                self.__datasource = JBrowseDataSource(self.base_jbrowse_path,self.track_folder)
            genes = []
            genes = self.__datasource.getGenes(chromosome, int(start), int(end), bool(isFeatures))
            retval = {'status': 'OK','genes':genes}
        except Exception,err:
            retval =  {"status":"ERROR","statustext":"%s" %str(err)}
        return retval

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getGeneDescription(self,gene):
        try:
            gene_parts = gene.split('.')
            description = self.gene_annotation[gene_parts[0]][gene]['functional_description']['computational_description']
            retval = {'status': 'OK','description':description}
        except Exception,err:
            retval =  {"status":"ERROR","statustext":"%s" %str(err)}
        return retval