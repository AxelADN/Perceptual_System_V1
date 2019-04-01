import json


class Spike(object):

    def __init__(self, name, modality, localization, intensity, duration):
        self._name = name
        self._modality = modality
        self._localization = localization
        self._intensity = intensity
        self._duration = duration

    def to_json(self):
        return json.dumps(self.__dict__)
