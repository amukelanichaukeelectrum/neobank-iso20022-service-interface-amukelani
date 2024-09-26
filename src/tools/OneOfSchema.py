

class OneOf(object):

    def __init__(self, json):
        properties = json[0]['properties']
        
        childList = {}
        for property in properties:
            childList[property] = self.extractSchemaName(properties[property]['$ref'])

        self.childList = childList

    def createObjects(self):
        return [(child, self.extractSchemaName(self.childList[child])) for child in self.childList]

    def extractSchemaName(self, ref):
        index = ref.rfind('/')
        return ref[index + 1:]

    def __str__(self):
        return str(self.childList)

    def __eq__(self, __o):
        return __o.childList == self.childList
