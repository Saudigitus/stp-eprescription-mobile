package org.saudigitus.e_prescription.network

object URLMapping {
    const val BASE_URL = "https://dhis2.gov.st/tracker"

    fun teiRelationshipUrl(
        tei: String,
        program: String,
        ouMode: String = "ACCESSIBLE"
    ): String {
        return "api/trackedEntityInstances.json?fields=relationships[relationshipType,from,to]&trackedEntityInstance=${tei}&program=${program}&ouMode=${ouMode}"
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
        return "api/trackedEntityInstances.json?fields=trackedEntityInstance,attributes[attribute,displayName,value]&trackedEntityInstance=${tei}&program=${program}&ouMode=${ouMode}"
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
        return "api/trackedEntityInstances.json?fields=trackerEntityInstance,enrollments[enrollment,events[event,programStage,status,dataValues[dataElement,value]]]&trackedEntityInstance=${tei}&program=${program}&ouMode=${ouMode}"
    }

    fun optionsUrl(code: String): String {
        return "api/options.json?fields=code,name,optionSet&filter=code:eq:${code}&filter=optionSet.id:eq:yPNaEEL1t7S&paging=false"
    }

    fun putEventUrl(
        event: String,
        dataElement: String,
    ): String {
        return "api/events/${event}/${dataElement}"
    }

    fun meUrl(baseUrl: String): String {
        val server = baseUrl.removeSuffix("/").trim()
        return "${server}/api/me.json"
    }
}