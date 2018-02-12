package com.numero.sojodia.contract

import com.numero.sojodia.model.*
import com.numero.sojodia.presenter.IPresenter
import com.numero.sojodia.view.IView

interface TimeTableContract {

    interface View : IView<Presenter> {

        fun showTimeTableRowList(timeTableRowList: MutableList<TimeTableRow>)

        fun showCurrentRoute(route: Route)

        fun showCurrentReciprocate(reciprocate: Reciprocate)
    }

    interface Presenter : IPresenter
}
