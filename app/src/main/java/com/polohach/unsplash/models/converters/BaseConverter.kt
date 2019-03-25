package com.polohach.unsplash.models.converters

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.SingleTransformer

abstract class BaseConverter<IN, OUT> : Converter<IN, OUT> {

    override fun convertInToOut(inObject: IN): OUT = processConvertInToOut(inObject)

    override fun convertOutToIn(outObject: OUT): IN = processConvertOutToIn(outObject)

    override fun convertListInToOut(inObjects: List<IN>?): List<OUT> =
            inObjects?.map { convertInToOut(it) } ?: listOf()

    override fun convertListOutToIn(outObjects: List<OUT>?): List<IN> =
            outObjects?.map { convertOutToIn(it) } ?: listOf()

    override fun convertOutToInRx(outObject: OUT): Flowable<IN?> =
            Flowable.just(convertOutToIn(outObject))

    override fun convertInToOutRx(inObject: IN): Flowable<OUT> =
            Flowable.just(convertInToOut(inObject))

    override fun convertListInToOutRx(inObjects: List<IN>): Flowable<List<OUT>> =
            Flowable.just(convertListInToOut(inObjects))

    override fun convertListOutToInRx(outObjects: List<OUT>): Flowable<List<IN>> =
            Flowable.just(convertListOutToIn(outObjects))

    override fun singleINtoOUT() =
            FlowableTransformer<OUT, IN> { out -> out.map { convertOutToIn(it) } }

    override fun singleOUTtoIN() =
            FlowableTransformer<IN?, OUT> { input -> input.map { convertInToOut(it) } }

    override fun listINtoOUT() =
            FlowableTransformer<List<OUT>, List<IN>> { out -> out.map { convertListOutToIn(it) } }

    override fun listOUTtoIN() =
            FlowableTransformer<List<IN>, List<OUT>> { input -> input.map { convertListInToOut(it) } }

    override fun singleINtoOUTSingle() =
            SingleTransformer<OUT, IN> { out -> out.map { convertOutToIn(it) } }

    override fun singleOUTtoINSingle() =
            SingleTransformer<IN?, OUT> { input -> input.map { convertInToOut(it) } }

    override fun listINtoOUTSingle() =
            SingleTransformer<List<OUT>, List<IN>> { out -> out.map { convertListOutToIn(it) } }

    override fun listOUTtoINSingle() =
            SingleTransformer<List<IN>, List<OUT>> { input -> input.map { convertListInToOut(it) } }

    protected abstract fun processConvertInToOut(inObject: IN): OUT

    protected abstract fun processConvertOutToIn(outObject: OUT): IN
}