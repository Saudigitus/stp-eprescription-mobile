package org.saudigitus.e_prescription.network

object URLMapping {
    const val BASE_URL = "https://dhis2.gov.st/tracker"

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
        return "/tracker/api/trackedEntityInstances.json?fields=trackerEntityInstance,enrollments[enrollment,events[event,programStage,status,dataValues[dataElement,value]]]&trackedEntityInstance=${tei}&program=${program}&ouMode=${ouMode}"
    }

    fun dataElementUrl(dataElement: String): String {
        return "/tracker/api/dataElements/${dataElement}.json?fields=id,displayFormName"
    }

    fun optionsUrl(code: String): String {
        return "/tracker/api/options.json?fields=code,name,optionSet&filter=code:eq:${code}&filter=optionSet.id:eq:yPNaEEL1t7S&paging=false"
    }

    fun putEventUrl(
        event: String,
        dataElement: String,
    ): String {
        return "/tracker/api/events/${event}/${dataElement}"
    }

    fun resourcesUrl(baseUrl: String) = "${baseUrl}/tracker/api/resources.json"
}