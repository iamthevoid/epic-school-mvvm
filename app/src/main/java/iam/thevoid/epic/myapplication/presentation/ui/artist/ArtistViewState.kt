package iam.thevoid.epic.myapplication.presentation.ui.artist

import iam.thevoid.epic.myapplication.data.model.musicbrnz.Artist


data class ArtistViewState(
    val input: String = "",
    val items: List<ArtistData> = emptyList(),
    val loading: Boolean = false,
    val pagesCount: Int = 0,
    val isFirstPage: Boolean = true
)

data class ArtistData(
    val artist: Artist,
    val flag: String?
)


//
//sealed class ArtisState {
//
//    object Loading: ArtisState()
//
//    data class Content(
//        val input: String = "",
//        val items: List<ArtistData> = emptyList(),
//        val loading: Boolean = false,
//        val pagesCount: Int = 0,
//        val isFirstPage: Boolean = true
//    ): ArtisState()
//}


//// Loading
//// Content
//// Content
//// Content
//// Content
//// Content
//// Content
//// Content
//
//fun setState(state: ArtisState)  {
//    when(state) {
//        is ArtisState.Loading -> showLoadingState()
//        is ArtisState.Content -> showContentState(state)
//    }
//}
//
//fun showLoadingState() {
//     /// do actions
//}
//
//
//fun showContentState(content: ArtisState.Content) {
//    /// do actions
//}


