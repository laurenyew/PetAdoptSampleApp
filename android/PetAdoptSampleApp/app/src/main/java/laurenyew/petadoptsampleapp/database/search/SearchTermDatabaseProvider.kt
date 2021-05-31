package laurenyew.petadoptsampleapp.database.search

interface SearchTermDatabaseProvider {
    suspend fun getAllSearchTerms(): List<SearchTerm>
    suspend fun getLastSearchTerm(): SearchTerm?
    suspend fun getSearchTerm(searchId: String): SearchTerm?
    suspend fun deleteSearchTerm(searchId: String)
    suspend fun insert(searchTerm: SearchTerm)
    suspend fun updateSearchTermTimeStamp(searchId: String)
}