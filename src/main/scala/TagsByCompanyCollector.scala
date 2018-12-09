object TagsByCompanyCollector {
  def getTags(companyName: String) = {
    val sites = BingAsker.searchSingle(companyName)
    val depth = 2
    // TODO: 1, если агрегатор
  }
}
