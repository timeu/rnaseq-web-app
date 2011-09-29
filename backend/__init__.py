'''
Created on Nov 16, 2010

@author: uemit.seren
'''

import os.path
import RNASeqService
import cherrypy
from cherrypy import _cperror
import simplejson

current_dir = os.path.dirname(os.path.abspath(__file__))
config_file = os.path.join(current_dir, 'development.conf')



def handle_error():
    cherrypy.response.status = 500
    cherrypy.response.body = "Unexpected Server Error"
    

def error_page_app(status, message, traceback, version):
    return str(message)

cherrypy.config.update({'error_page.500': error_page_app})

class Root:
    _cp_config = {'request.error_response': handle_error}
    
    @cherrypy.expose
    def index(self,**params):
        accession_list = self.gwas.getAccessions()
        json_accessions = simplejson.dumps(accession_list,separators=(',',':'))
        json_locationdistribution = self.gwas.getLocationDistributionData()
        json_chr_sizes = [30424404,23454534,19692149,26973736,18577244]
        content = """
<!doctype html>
<html>
<head>
    <script type="text/javascript" language="javascript">
        var clientData = {
            accessions:'%s',
            locationdistribution:%s,
            chr_sizes:'%s'
        };
    </script>
    <script type="text/javascript" language="javascript" src="http://www.google.com/jsapi" > </script>
    <script type="text/javascript" language="javascript" src="/gwt/rnaseqwebapp.nocache.js"></script>
    
    <title>RNA-Seq Browser</title>
</head>
<body>
    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>
    </body>
</html>"""
        return content % (json_accessions,json_locationdistribution,json_chr_sizes)
        
        
        
        #raise cherrypy.HTTPRedirect("/index.html")




  

root = Root()
root.gwas = RNASeqService.RNASeqService()

#cherrypy.tree.mount(root,config=config_file)


if __name__ == '__main__':
    cherrypy.quickstart(root,config=config_file)


