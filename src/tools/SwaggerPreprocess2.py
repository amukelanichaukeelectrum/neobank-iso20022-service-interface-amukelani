import yaml
from sys import argv
from yaml import Loader

FILENAME = "neobank.2.swagger.yaml"
OUTPUT = "output.swagger.yaml"
number = 0


def readFile(fileName):
    f = open(fileName)
    return yaml.load(f, Loader=Loader)


def preprocess(fileName):
    data = readFile(fileName)

    components = data['components']
    schemas = components['schemas']

    preprocessSchemas(schemas)

    writePreprocessedSchemas(data)


def preprocessSchemas(schemas):
    schemaNames = [schema for schema in schemas]

    for schema in schemaNames:
        if schema[-6:] == "Choice":
            preprocessChoice(schemas, schemas[schema])


def preprocessChoice(schemas, schema):
    oneOf = schema['oneOf']
    properties = oneOf[0]['properties']

    for property in properties:
        properties[property]['$ref']

    refs = []
    for property in properties:
        refs.append({"$ref": properties[property]['$ref']})

    refs = [{"$ref": getRefWrapper(schemas, property, properties[property]['$ref'])} \
            for property in properties]

    schema['oneOf'] = refs
    schema['x-is-one-of-class'] = True


def getRefWrapper(schemas, property, ref):
    wrapper = "{}{}".format(property[0].upper() + property[1:], extractSchemaName(ref))
    try:
        schemas[wrapper]
    except KeyError:
        properties = {property: {"$ref": ref}}
        wrappedObject = {'type': 'object', 'properties': properties}
        schemas[wrapper] = wrappedObject

    return "#/components/schemas/{}".format(wrapper)


def extractSchemaName(ref):
    index = ref.rfind('/')
    return ref[index + 1:]


def writePreprocessedSchemas(notOneOfList):
    f = open(OUTPUT, "w")
    yaml.dump(notOneOfList, f, default_flow_style=False, sort_keys=False)


if __name__ == "__main__":
    if len(argv) != 2:
        print("Usage: python3 SwaggerPreprocess2.py <inputFile>")
        exit()

    preprocess(argv[1])
