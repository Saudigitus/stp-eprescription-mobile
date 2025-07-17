package org.saudigitus.e_prescription.network

object URLMapping {
    const val BASE_URL = "https://dhis2.gov.st/tracker"

    /**
     * This retrieves small info of the program such as:
     * id, name, programTrackedEntityAttributes, programStages
     */
    fun programDetails(program: String): String {
        return "programs/${program}.json?fields=id,name,programTrackedEntityAttributes[trackedEntityAttribute[id,name,formName]],programStages[id,name,programStageDataElements[dataElement[id,name,formName]]]"
    }

    fun teiRelationshipUrl(
        tei: String,
        program: String,
        ouMode: String = "ACCESSIBLE"
    ): String {
        return "/tracker/api/trackedEntityInstances.json?fields=relationships[relationshipType,from,to]&trackedEntityInstance=${tei}&program=${program}&ouMode=${ouMode}"
    }

    /**
     * This retrieves Tracked Entity Instances attributes
     * @param tei The Tracked Entity Instance ID
     * @param program The Program ID
     * @param ouMode The Organisation Unit Mode
     */
    fun teiAttributesUrl(
        tei: String,
        program: String,
        ouMode: String = "ACCESSIBLE"
    ): String {
        return "/tracker/api/trackedEntityInstances.json?fields=trackedEntityInstance,attributes[attribute,displayName,value]&trackedEntityInstance=${tei}&program=${program}&ouMode=${ouMode}"
    }

    /**
     * This retrieves Tracked Entity Instances events
     * @param tei The Tracked Entity Instance ID
     * @param program The Program ID
     * @param ouMode The Organisation Unit Mode
     */
    fun teiEventsUrl(
        tei: String,
        program: String,
        ouMode: String = "ACCESSIBLE"
    ): String {
        return "trackedEntityInstances.json?fields=trackerEntityInstance,enrollments[enrollment,events[event,programStage,status,dataValues[dataElement,value]]]&trackedEntityInstance=${tei}&program=${program}&ouMode=${ouMode}"
    }

    fun dataElementUrl(dataElement: String): String {
        return "dataElements/${dataElement}.json?fields=id,displayFormName"
    }

    fun optionsUrl(code: String): String {
        return "options.json?fields=code,name,optionSet&filter=code:eq:${code}&filter=optionSet.id:eq:yPNaEEL1t7S&paging=false"
    }
}